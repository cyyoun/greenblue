package cyy.greenblue.controller;

import cyy.greenblue.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("point")
public class PointController {

    private final PointService pointService;

    @GetMapping("/member/{memberId}")
    public void points(@PathVariable long memberId) {

    }
}
