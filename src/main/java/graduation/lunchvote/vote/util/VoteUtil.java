package graduation.lunchvote.vote.util;

import graduation.lunchvote.vote.model.Vote;
import graduation.lunchvote.vote.to.VoteTo;

import java.util.Collection;
import java.util.List;

public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::createTo)
                .toList();
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDate(), vote.getUser().getId(), vote.getRestaurant().getId());
    }
}
