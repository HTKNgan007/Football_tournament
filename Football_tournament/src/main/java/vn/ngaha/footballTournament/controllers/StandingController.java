//package vn.ngaha.footballTournament.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import vn.ngaha.footballTournament.models.Matches;
//import vn.ngaha.footballTournament.models.Standings;
//import vn.ngaha.footballTournament.models.Tournaments;
//import vn.ngaha.footballTournament.repositories.TournamentRepository;
//import vn.ngaha.footballTournament.services.impl.StandingService;
//
//@Controller
//public class StandingController {
//	@Autowired
//    private StandingService standingService;
//
//    @Autowired
//    private TournamentRepository tournamentRepository;
//
//    @GetMapping("/tournaments/{id}/standings")
//    public String showStandings(@PathVariable Long id, Model model) {
//        Tournaments tournament = tournamentRepository.findById(id).orElseThrow();
//        List<Matches> matches = standingService.getMatchesByTournament(tournament);
//        List<Standings> standings = standingService.calculateStandings(matches);
//
//        model.addAttribute("tournament", tournament);
//        model.addAttribute("matches", matches);
//        model.addAttribute("standings", standings);
//
//        return "results-standings";
//    }
//}
