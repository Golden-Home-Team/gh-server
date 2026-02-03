package kr.co.goldenhome

import kr.co.goldenhome.event.EventDeduplicationManager
import kr.co.goldenhome.model.FacilityEvent
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DeduplicateEventAspectSpec extends Specification {

    def eventDeduplicationManager = Mock(EventDeduplicationManager)
    def aspect = new DeduplicateEventAspect(eventDeduplicationManager)

    def "동시에 동일한 이벤트가 발생했을 때 하나만 실행되고 나머지는 차단되어야 한다"() {
        given:
        def event = Mock(FacilityEvent)
        event.getEventId() >> "test-event-123"

        def signature = Mock(MethodSignature)
        signature.getName() >> "processViewEvent"

        def id = "test-event-123-processViewEvent"

        def startLatch = new CountDownLatch(1)
        def finishLatch = new CountDownLatch(2)
        def executor = Executors.newFixedThreadPool(2)
        def results = Collections.synchronizedList([])

        and: "Deduplication 동작 정의"
        1 * eventDeduplicationManager.saveLog(id) >> { }
        1 * eventDeduplicationManager.saveLog(id) >> {
            throw new DataIntegrityViolationException("Duplicate Key")
        }

        when:
        (1..2).each {
            executor.submit {
                // 스레드마다 joinPoint 새로 생성
                def joinPoint = Mock(ProceedingJoinPoint)
                joinPoint.getArgs() >> [event]
                joinPoint.getSignature() >> signature

                // proceed는 첫 번째만 성공
                joinPoint.proceed() >> "Success"

                try {
                    startLatch.await()
                    def result = aspect.around(joinPoint)
                    results << (result == null ? "IS_NULL" : result)
                } finally {
                    finishLatch.countDown()
                }
            }
        }

        startLatch.countDown()
        finishLatch.await(3, TimeUnit.SECONDS)

        then:
        results.size() == 2
        results.count { it == "Success" } == 1
        results.count { it == "IS_NULL" } == 1
    }

}
