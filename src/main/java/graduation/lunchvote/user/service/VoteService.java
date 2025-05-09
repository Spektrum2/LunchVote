package graduation.lunchvote.user.service;

import graduation.lunchvote.common.error.IllegalRequestDataException;
import graduation.lunchvote.common.error.NotFoundException;
import graduation.lunchvote.user.model.Restaurant;
import graduation.lunchvote.user.model.User;
import graduation.lunchvote.user.model.Vote;
import graduation.lunchvote.user.repository.RestaurantRepository;
import graduation.lunchvote.user.repository.UserRepository;
import graduation.lunchvote.user.repository.VoteRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VoteService {
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Выполняет операцию голосования для пользователя за указанный ресторан на текущую дату.
     * <p>
     * Метод определяет сегодняшнюю дату и текущее время и пытается найти уже существующий голос пользователя за выбранную дату.
     * Если голос уже существует, то:
     * <ul>
     *   <li>Если текущее время позже 11:00, изменение голоса не допускается и выбрасывается исключение
     *       {@link IllegalRequestDataException} с соответствующим сообщением.</li>
     *   <li>Если текущее время до 11:00, голос обновляется новым выбором ресторана, и изменения сохраняются в базе данных.</li>
     * </ul>
     * Если голос за сегодняшний день отсутствует, создается новая голосовая запись с сегодняшней датой, ассоциированная с указанными
     * пользователем и рестораном.
     * </p>
     *
     * @param userId идентификатор пользователя, который совершает голосование.
     * @param restaurantId идентификатор ресторана, за который пользователь отдает свой голос.
     * @return объект {@link Vote}, представляющий голос пользователя, сохраненный в базе данных.
     * @throws IllegalRequestDataException если попытка изменения уже существующего голоса происходит после 11:00.
     */

    @CacheEvict(cacheNames = "votes", allEntries = true)
    @Transactional
    public Vote vote(Integer userId, Integer restaurantId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        User user = userRepository.getExisted(userId);

        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);

        Optional<Vote> existingVote = voteRepository.findByUserIdAndDate(userId, today);

        if (existingVote.isPresent()) {
            if (now.isAfter(LocalTime.of(11, 0))) {
                throw new IllegalRequestDataException("Vote cannot be changed after 11:00 AM");
            }
            Vote vote = existingVote.get();
            vote.setRestaurant(restaurant);
            return voteRepository.save(vote);
        }

        Vote newVote = new Vote(null, today, user, restaurant);
        return voteRepository.save(newVote);
    }

    /**
     * Возвращает результаты голосования за указанную дату.
     *
     * <p>
     * Если переданный параметр {@code date} не равен {@code null}, используется указанная дата; в противном случае применяется текущая дата,
     * полученная через {@code LocalDate.now()}. Далее происходит извлечение всех голосов за выбранную дату из репозитория голосований.
     * Если список голосов пуст, метод возвращает список с единственным сообщением о том, что голосов за данную дату не найдено.
     * Если голоса присутствуют, метод группирует их по идентификатору ресторана, подсчитывает количество голосов для каждого ресторана,
     * сортирует результаты по убыванию количества голосов и преобразует их в список строк с форматом: "Ресторан {id} : Рейтинг {количество голосов}".
     * </p>
     *
     * @param date дата, для которой необходимо получить результаты голосования. Если {@code null}, используется текущая дата.
     * @return список строк, каждая из которых содержит информацию о рейтинге ресторана по его идентификатору, либо сообщение о том, что голосов за
     *         указанную дату не найдено.
     */
    @Cacheable(cacheNames = "votes")
    public List<String> getVotingResults(LocalDate date) {
        LocalDate queryDate = (date != null) ? date : LocalDate.now();

        List<Vote> votes = voteRepository.findAllByDate(queryDate);

        if (votes.isEmpty()) {
            return Collections.singletonList("Нет голосов на дату: " + queryDate);
        }

        return votes.stream()
                .collect(Collectors.groupingBy(v -> v.getRestaurant().getId(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> String.format("Ресторан %d : Рейтинг %d", entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех голосов, зарегистрированных в системе.
     *
     * @return список объектов {@link Vote}, представляющих все голоса
     */
    @Cacheable(cacheNames = "votes")
    public List<Vote> getAll() {
        return voteRepository.getAll();
    }

    public Vote get(int id) {
        return voteRepository.get(id)
                .orElseThrow(() -> new NotFoundException("Vote with id=" + id + " not found"));
    }

    /**
     * Удаляет голос с указанным идентификатором из системы.
     *
     * @param id идентификатор голоса, который необходимо удалить.
     */
    @CacheEvict(cacheNames = "votes", allEntries = true)
    public void delete(int id) {
        voteRepository.deleteExisted(id);
    }
}
