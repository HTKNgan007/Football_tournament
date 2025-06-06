package vn.ngaha.footballTournament.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Standings;
import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.repositories.MatchResultRepository;
import vn.ngaha.footballTournament.repositories.StandingRepository;
import vn.ngaha.footballTournament.repositories.TeamRepository;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.MatchService;
import vn.ngaha.footballTournament.services.TournamentService;
import vn.ngaha.footballTournament.services.impl.StandingServiceImpl;

@Controller
public class TournamentController {
	@Autowired
    private MatchService matchService;

    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchRepository matchesRepository;

    @Autowired
    private MatchResultRepository matchResultRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private StandingRepository standingRepository;
    @Autowired
    private StandingServiceImpl standingService;

    @PostMapping("/tournaments/{id}/generate-schedule")
    public String generateSchedule(@PathVariable Long id) {
        matchService.generateSchedule(id);
        return "redirect:/tournaments/" + id;
    }
    
    @GetMapping("/tournaments/{id}/matches")
    public String showMatches(@PathVariable Long id, Model model) {
        Tournaments tournament = tournamentRepository.findById(id).orElseThrow();
        List<Matches> matches = matchesRepository.findByTournament(tournament);

        matches.sort(Comparator.comparing(Matches::getMatchDate));
        model.addAttribute("tournament", tournament);
        model.addAttribute("matches", matches);

        return "matches-list";
    }
    
	@PostMapping("/tournaments/{id}/delete")
	public String deleteTournament(@PathVariable Long id) {
	    Tournaments tournament = tournamentRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giải đấu"));
    // 1. Xoá MatchResults
    List<Matches> matches = matchesRepository.findByTournament(tournament);
    for (Matches match : matches) {
        if (match.getResult() != null) {
            matchResultRepository.delete(match.getResult());
        }
    }
    // 2. Xoá Matches
    matchesRepository.deleteAll(matches);
    // 3. Xoá Standings (nếu có)
    standingRepository.deleteByTournament(tournament);
    // 4. Xoá Teams
    List<Teams> teams = teamRepository.findByTournament(tournament);
    teamRepository.deleteAll(teams);
    // 5. Cuối cùng xoá Tournament
    tournamentRepository.delete(tournament);

    return "redirect:/";
}
	
	@GetMapping("/tournaments/{id}/results")
	public String showResults(@PathVariable Long id, Model model) {
	    Tournaments tournament = tournamentRepository.findById(id).orElseThrow();
	    List<Matches> matches = standingService.getMatchesByTournament(tournament);

	    model.addAttribute("tournament", tournament);
	    model.addAttribute("matches", matches);

	    return "tournament-results";
	}

	@GetMapping("/tournaments/{id}/standings")
	public String showStandings(@PathVariable Long id, Model model) {
	    Tournaments tournament = tournamentRepository.findById(id).orElseThrow();
	    List<Matches> matches = standingService.getMatchesByTournament(tournament);
	    List<Standings> standings = standingService.calculateStandings(tournament, matches);

	    model.addAttribute("tournament", tournament);
	    model.addAttribute("standings", standings);

	    return "tournament-standing";
	}
	
	@PostMapping("/tournaments/{tournamentId}/remove-team/{teamId}")
	public String removeTeamFromTournament(@PathVariable Long tournamentId, @PathVariable Long teamId) {
	    System.out.println("Xoá team " + teamId + " khỏi giải " + tournamentId);
	    tournamentService.removeTeam(tournamentId, teamId);
	    return "redirect:/tournaments/" + tournamentId;
	}

}
