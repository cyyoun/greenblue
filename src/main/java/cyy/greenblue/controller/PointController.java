package cyy.greenblue.controller;

import cyy.greenblue.domain.Point;
import cyy.greenblue.dto.PointDto;
import cyy.greenblue.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private final PointService pointService;

    @GetMapping("/member/{memberId}")
    public int currentPoint(@PathVariable long memberId) {
        return pointService.currentPoint(memberId);
    }

    @GetMapping("/points/member/{memberId}")
    public List<PointDto> points(@PathVariable long memberId) {
        return pointService.findAllByMemberId(memberId).stream()
                .map(point -> new PointDto(
                        point.getId(),
                        point.getPoints(),
                        point.getReview() != null ? point.getReview().getId() : 0,
                        point.getOrderProduct() != null ? point.getOrderProduct().getId() : 0
                ))
                .collect(Collectors.toList());

    }
}
