package com.example.testTaskRoomservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MainController {
    @Autowired
    private UserActionsRepository userActionsRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/check")
    public ResponseEntity<String> check(@RequestParam(name = "roomId") Integer roomId, @RequestParam(name = "keyId") Integer keyId,
                                        @RequestParam(name = "entrance") boolean entrance) {
        String errorText = "500 ошибка";
        //IDs can't be less than 0
        if (roomId > 0 && keyId > 0) {
            try {
                UserAction newAction = new UserAction(roomId, keyId, entrance);
                boolean result = calculateEntranceAbility(newAction);
                String openDoorText = "200 - дверь можно открыть";
                String banDoorText = "403 - запрет на вход";
                newAction.setResponse(result ? openDoorText : banDoorText);
                userActionsRepository.save(newAction);
                return new ResponseEntity<>(newAction.getResponse(), result ? HttpStatus.OK : HttpStatus.FORBIDDEN);
            } catch (Exception e) {
                return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else return new ResponseEntity<>(errorText, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //just for test myself
    @GetMapping(path = "/allActions")
    public Iterable<UserAction> getAllUserActions() {
        return userActionsRepository.findAll();
    }

    //just for test myself
    @GetMapping(path = "/allUsers")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    //while user in one of rooms he can't enter in new room
    public boolean checkUserInRoom(Integer UserId, Integer roomId) {
        Optional<User> user = userRepository.findById(UserId);
        if (user.isPresent()) {
            return !user.get().getRoomId().equals(roomId) && 0 != user.get().getRoomId();
        } else {
            User newUser = new User(UserId,roomId);
            userRepository.save(newUser);
            return false;
        }
    }

    //the user can only enter those rooms, the number of which is divided into his ID.
    public boolean checkUserRights(Integer roomId, Integer keyId) {
        return keyId % roomId == 0;
    }

    //no restrictions on leave the room. user in base always can leave
    public boolean calculateEntranceAbility(UserAction newAction) {
        if (!newAction.getEntrance()) {
            //but if user doesn't exist in database, I think it should be banned from leaving the room
            Optional<User> user = userRepository.findById(newAction.getKeyId());
            if (user.isPresent() && (user.get().getRoomId().equals(newAction.getRoomId()))) {
                user.get().setRoomId(0);
                return true;
            } else return false;
        } else if (this.checkUserRights(newAction.getRoomId(), newAction.getKeyId()) && !this.checkUserInRoom(newAction.getKeyId(), newAction.getRoomId())) {
            userRepository.findById(newAction.getKeyId()).ifPresent((theUser -> theUser.setRoomId(newAction.getRoomId())));
            return true;
        } else return false;
    }

}