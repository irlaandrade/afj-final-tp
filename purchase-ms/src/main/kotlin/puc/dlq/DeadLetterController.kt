package puc.dlq

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dlq")
class DeadLetterController(val dlqService: DlqService) {

    @GetMapping("/send")
    fun sendMessage(@RequestParam message: String): String {
        dlqService.sendDlqMessage(message)
        return "Message sent!"
    }
}