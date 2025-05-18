package vn.ngaha.footballTournament.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.ngaha.footballTournament.models.Matches;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.MatchRepository;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.MatchService;

@Controller
public class TournamentController {
	@Autowired
    private MatchService matchService;
	
	@Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MatchRepository matchesRepository;

    @PostMapping("/tournaments/{id}/generate-schedule")
    public String generateSchedule(@PathVariable Long id) {
        matchService.generateSchedule(id);
        return "redirect:/tournaments/" + id;
    }
    
    @GetMapping("/tournaments/{id}/matches")
    public String showMatches(@PathVariable Long id, Model model) {
        Tournaments tournament = tournamentRepository.findById(id).orElseThrow();
        List<Matches> matches = matchesRepository.findByTournament(tournament);

        model.addAttribute("tournament", tournament);
        model.addAttribute("matches", matches);

        return "matches-list";
    }


}
