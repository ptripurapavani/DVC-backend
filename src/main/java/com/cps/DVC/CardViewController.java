package com.cps.DVC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/views")
public class CardViewController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ Get view count for a specific card
    @GetMapping("/{id}")
    public int getViewCount(@PathVariable("id") Long cardId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT views FROM card_views WHERE card_id = ?",
                Integer.class,
                cardId
            );
        } catch (Exception e) {
            return 0;
        }
    }

    // ✅ Increment view count (insert or update safely)
    @PostMapping("/{id}")
    public int incrementView(@PathVariable("id") Long cardId) {
        jdbcTemplate.update("""
            INSERT INTO card_views (card_id, views)
            VALUES (?, 1)
            ON CONFLICT (card_id) DO UPDATE SET views = card_views.views + 1
        """, cardId);

        return jdbcTemplate.queryForObject(
            "SELECT views FROM card_views WHERE card_id = ?",
            Integer.class,
            cardId
        );
    }
}
