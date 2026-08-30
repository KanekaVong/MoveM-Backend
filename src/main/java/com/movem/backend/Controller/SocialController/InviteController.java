package com.movem.backend.Controller.SocialController;


import com.movem.backend.Dto.response.FriendResponse.InviteResponse;
import com.movem.backend.Service.FriendServices.InviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    public ResponseEntity<InviteResponse> createInvite() {

        return ResponseEntity.ok(
                inviteService.createInvite()
        );
    }

    @GetMapping("/{token}")
    public ResponseEntity<InviteResponse> getInvite(
            @PathVariable String token
    ) {

        return ResponseEntity.ok(
                inviteService.getInvite(token)
        );
    }

    @PostMapping("/{token}/accept")
    public ResponseEntity<Void> acceptInvite(
            @PathVariable String token
    ) {

        inviteService.acceptInvite(
                token
        );

        return ResponseEntity.ok().build();
    }
}