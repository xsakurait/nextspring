package com.youtube.channelmanager.controller;

import com.youtube.channelmanager.dto.VideoDTO;
import com.youtube.channelmanager.entity.User;
import com.youtube.channelmanager.repository.UserRepository;
import com.youtube.channelmanager.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final UserRepository userRepository;

    @GetMapping("/recent")
    public ResponseEntity<List<VideoDTO>> getRecentVideos(
            Authentication authentication,
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "50") int limit) {

        User user = getUserFromAuthentication(authentication);
        List<VideoDTO> videos = videoService.getRecentVideosForUser(user.getId(), days, limit);
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/recent/category/{categoryId}")
    public ResponseEntity<List<VideoDTO>> getRecentVideosByCategory(
            Authentication authentication,
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "50") int limit) {

        User user = getUserFromAuthentication(authentication);
        List<VideoDTO> videos = videoService.getRecentVideosByCategory(
            user.getId(), categoryId, days, limit
        );
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/recent/sorted")
    public ResponseEntity<List<VideoDTO>> getRecentVideosSorted(
            Authentication authentication,
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "publishedAt") String sortBy,
            @RequestParam(defaultValue = "50") int limit) {

        User user = getUserFromAuthentication(authentication);
        List<VideoDTO> videos = videoService.getRecentVideosWithSort(
            user.getId(), days, sortBy, limit
        );
        return ResponseEntity.ok(videos);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncVideos(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        videoService.syncLatestVideos(user.getId());
        return ResponseEntity.ok("Video sync started");
    }

    @PutMapping("/{videoId}/update-stats")
    public ResponseEntity<VideoDTO> updateVideoStats(@PathVariable String videoId) {
        VideoDTO video = videoService.updateVideoStats(videoId);
        return ResponseEntity.ok(video);
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
