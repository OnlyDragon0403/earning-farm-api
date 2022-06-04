package tech.yeez.investment.service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.yeez.investment.model.dto.common.Result;
import tech.yeez.investment.model.dto.request.InfuraCallRequestDto;
import tech.yeez.investment.model.dto.request.SubscribeRequestDto;

@FeignClient(name = "subscription-client", url = "${subscription.service.url}")
public interface ISubscriptionService {

    @PostMapping("/event/query/transaction/firstlog")
    Result<String> ethGetTransactionFirstLog(@RequestParam(required = false) String netWork, @RequestParam(required = false) String transactionHash);

    @PostMapping("/event/query/transaction")
    Result<String> ethGetTransactionReceipt(@RequestParam(required = false) String netWork, @RequestParam(required = false) String transactionHash);

    @PostMapping(value = "/event/subscribe")
    Result<String> subscripe(@RequestBody(required = false) SubscribeRequestDto subscribe);

    @PostMapping(value = "/event/subscribe/status")
    Result<String> subscripeStatus(@RequestParam(required = false) String subId);

    @PostMapping(value = "/event/call")
    Result<String> infuraCall(@RequestBody(required = false) InfuraCallRequestDto infuraCallRequestDto);

    @PostMapping(value = "/event/subscribe/heigh")
    Result<String> subscripeHeight(@RequestParam(required = false) String subId);

}
