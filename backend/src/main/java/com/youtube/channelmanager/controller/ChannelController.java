package com.youtube.channelmanager.controller;

import com.youtube.channelmanager.dto.ChannelDTO;
import com.youtube.channelmanager.entity.User;
import com.youtube.channelmanager.repository.UserRepository;
import com.youtube.channelmanager.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ChannelDTO>> getUserChannels(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        List<ChannelDTO> channels = channelService.getUserChannels(user.getId());
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ChannelDTO>> getChannelsByCategory(
            Authentication authentication,
            @PathVariable Long categoryId) {

        User user = getUserFromAuthentication(authentication);
        List<ChannelDTO> channels = channelService.getChannelsByCategory(user.getId(), categoryId);
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<ChannelDTO> subscribeChannel(
            Authentication authentication,
            @RequestBody Map<String, Object> request) {

        User user = getUserFromAuthentication(authentication);
        String channelId = (String) request.get("channelId");
        Long categoryId = request.get("categoryId") != null ?
            Long.valueOf(request.get("categoryId").toString()) : null;

        ChannelDTO channel = channelService.subscribeChannel(user.getId(), channelId, categoryId);
        return ResponseEntity.ok(channel);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<?> unsubscribeChannel(
            Authentication authentication,
            @PathVariable Long channelId) {

        User user = getUserFromAuthentication(authentication);
        channelService.unsubscribeChannel(user.getId(), channelId);
        return ResponseEntity.ok("Channel unsubscribed");
    }

    @PutMapping("/{channelId}/category")
    public ResponseEntity<?> updateChannelCategory(
            Authentication authentication,
            @PathVariable Long channelId,
            @RequestBody Map<String, Long> request) {

        User user = getUserFromAuthentication(authentication);
        Long categoryId = request.get("categoryId");
        channelService.updateChannelCategory(user.getId(), channelId, categoryId);
        return ResponseEntity.ok("Category updated");
    }

    private User getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
