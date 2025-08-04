package kr.co.goldenhome.account.service

import kr.co.goldenhome.entity.User
import kr.co.goldenhome.infrastructure.UserRepository
import spock.lang.Specification

class AccountServiceSpec extends Specification {

    AccountService accountService
    UserRepository userRepository = Mock()

    def setup() {
        accountService = new AccountService(userRepository)
    }

    def "withdraw - userRepository 를 호출한다"() {

        when:
        accountService.withdraw(1L)

        then:
        1 * userRepository.findById(*_) >> {
            Long userId ->
                userId == 1L
                Optional.of(User.builder().build())
        }
    }
}
