package vn.ngaha.footballTournament.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Teams {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournaments tournament;

    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private List<Matchs> matchesAsTeam1;

    @OneToMany(mappedBy = "team2", cascade = CascadeType.ALL)
    private List<Matchs> matchesAsTeam2;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private Standings standing;

	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tournaments getTournament() {
		return tournament;
	}

	public void setTournament(Tournaments tournament) {
		this.tournament = tournament;
	}

	public List<Matchs> getMatchesAsTeam1() {
		return matchesAsTeam1;
	}

	public void setMatchesAsTeam1(List<Matchs> matchesAsTeam1) {
		this.matchesAsTeam1 = matchesAsTeam1;
	}

	public List<Matchs> getMatchesAsTeam2() {
		return matchesAsTeam2;
	}

	public void setMatchesAsTeam2(List<Matchs> matchesAsTeam2) {
		this.matchesAsTeam2 = matchesAsTeam2;
	}

	public Standings getStanding() {
		return standing;
	}

	public void setStanding(Standings standing) {
		this.standing = standing;
	}

}
