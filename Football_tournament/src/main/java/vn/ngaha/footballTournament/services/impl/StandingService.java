package vn.ngaha.footballTournament.services.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.models.Standings;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.repositories.TournamentRepository;

@Service
public class StandingService {
	@Autowired
    private MatchRepository matchesRepository;

    public List<Matches> getMatchesByTournament(Tournaments tournament) {
        return matchesRepository.findByTournament(tournament);
    }

    public List<Standings> calculateStandings(List<Matches> matches) {
        // Viết logic xử lý bảng xếp hạng tại đây, trả về list Standings
        // Tạm thời trả về rỗng để compile được
        return List.of();
    }
}
