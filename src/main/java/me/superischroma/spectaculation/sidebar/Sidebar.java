package me.superischroma.spectaculation.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class Sidebar
{
    private static ScoreboardManager manager = Bukkit.getScoreboardManager();

    private String name;
    private String identifier;

    private Scoreboard board;
    private Objective obj;
    private List<Score> scores;

    public Sidebar(String name, String identifier)
    {
        this.name = name;
        this.identifier = identifier;
        this.board = manager.getNewScoreboard();
        this.obj = board.registerNewObjective(identifier, "");
        this.scores = new ArrayList<>();
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(name);
    }

    public void add(String s)
    {
        Score score = obj.getScore(s);
        scores.add(0, score);
    }

    public void apply(Player player)
    {
        for (int i = 0; i < scores.size(); i++)
            scores.get(i).setScore(i);
        player.setScoreboard(board);
    }
}