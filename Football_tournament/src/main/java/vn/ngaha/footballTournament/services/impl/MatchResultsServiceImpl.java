package vn.ngaha.footballTournament.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngaha.footballTournament.models.MatchResults;
import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.repositories.MatchResultRepository;

@Service
public class MatchResultsServiceImpl {

	@Autowired
    private MatchResultRepository matchResultsRepository;

    public MatchResults getOrCreateResultByMatch(Matches match) {
        return matchResultsRepository.findByMatch(match).orElseGet(() -> {
            MatchResults newResult = new MatchResults();
            newResult.setMatch(match);
            return newResult;
        });
    }

    public void saveResult(MatchResults result) {
        matchResultsRepository.save(result);
    }
}
