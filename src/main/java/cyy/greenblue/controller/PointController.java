package cyy.greenblue.controller;

import cyy.greenblue.dto.PointDto;
import cyy.greenblue.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PointController {

    private final PointService pointService;

    @GetMapping("/point")
    public int currentPoint(Authentication authentication) {
        return pointService.currentPointByAuthentication(authentication);
    }

    @GetMapping("/points")
    public List<PointDto> points(Authentication authentication) {
        return pointService.findAllByAuthentication(authentication);
    }
}
