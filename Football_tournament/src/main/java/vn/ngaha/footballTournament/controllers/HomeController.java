package vn.ngaha.footballTournament.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.ngaha.footballTournament.models.Teams;
import vn.ngaha.footballTournament.models.Tournaments;
import vn.ngaha.footballTournament.repositories.TournamentRepository;
import vn.ngaha.footballTournament.services.TeamService;
import vn.ngaha.footballTournament.services.TournamentService;

@Controller
public class HomeController {

	@Autowired
    private TournamentService tournamentService;
	@Autowired
	private TournamentRepository tournamentRepository;
	@Autowired
	private TeamService teamService;

	@GetMapping("/")
	public String home(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
	    List<Tournaments> tournaments;

	    if (keyword != null && !keyword.isEmpty()) {
	        tournaments = tournamentRepository.findByNameContainingIgnoreCase(keyword);
	    } else {
	        tournaments = tournamentService.getAllTournaments();
	    }

	    model.addAttribute("tournaments", tournaments);
	    model.addAttribute("keyword", keyword);

	    return "home";
	}

    
    @GetMapping("/tournaments/add")
    public String showAddTournamentForm(Model model) {
        model.addAttribute("tournament", new Tournaments());
        return "add-tournament";
    }

    @GetMapping("/tournaments/edit/{id}")
    public String showEditTournamentForm(@PathVariable Long id, Model model) {
        Tournaments tournament = tournamentService.getTournamentById(id);
        model.addAttribute("tournament", tournament);
        return "edit-tournament";
    }

    @PostMapping("/tournaments/update")
    public String updateTournament(@ModelAttribute Tournaments tournament) {
        tournamentService.saveTournament(tournament); // vì save() sẽ update nếu có ID
        return "redirect:/";
    }
    
    @PostMapping("/tournaments/save")
    public String saveTournament(@ModelAttribute Tournaments tournament) {
        tournamentService.saveTournament(tournament);
        return "redirect:/";
    }
    
    @GetMapping("/tournaments/{id}")
    public String viewTournamentDetails(@PathVariable Long id, Model model) {
        Tournaments tournament = tournamentService.getTournamentById(id);
        List<Teams> teams = teamService.getTeamsByTournament(tournament);

        model.addAttribute("tournament", tournament);
        model.addAttribute("teams", teams);
        model.addAttribute("newTeam", new Teams());

        return "tournament-detail";
    }

    @PostMapping("/tournaments/{id}/add-team")
    public String addTeamToTournament(@PathVariable Long id, @ModelAttribute("newTeam") Teams team) {
        Tournaments tournament = tournamentService.getTournamentById(id);
        team.setId(null);
        team.setTournament(tournament);
        teamService.saveTeam(team);
        return "redirect:/tournaments/" + id;
    }
}
