package cyy.greenblue.service;

import cyy.greenblue.domain.OrderSheet;
import cyy.greenblue.repository.OrderSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderSheetService {

    private final OrderSheetRepository orderSheetRepository;

    public void add(List<OrderSheet> orderSheet) {
        /**
         * 결제 로직 필요
         */

        orderSheetRepository.saveAll(orderSheet);
    }


}
