package daggerok.vote

import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VoteAggregatorTest {

  lateinit var fixture: FixtureConfiguration<VoteAggregator>

  @Before fun setUp() {
    fixture = AggregateTestFixture(VoteAggregator::class.java)
  }

  @Test fun `should register candidate`() {
    fixture.givenNoPriorActivity()
        .`when`(RegisterCandidateCommand(candidateId = "candidateId", name = "Candidate Name"))
        .expectEvents(CandidateRegisteredEvent(candidateId = "candidateId", name = "Candidate Name"))
  }

  @Test fun `should be able to approve candidate registration`() {
    fixture.given(CandidateRegisteredEvent(candidateId = "candidateId", name = "Candidate Name"))
        .`when`(ApproveRegistrationCommand(candidateId = "candidateId"))
        .expectEvents(RegistrationApprovedEvent(candidateId = "candidateId"))
  }

  @Test fun `should be able to decline candidate registration`() {
    fixture.given(CandidateRegisteredEvent(candidateId = "candidateId", name = "Candidate Name"),
        RegistrationApprovedEvent(candidateId = "candidateId"))
        .`when`(DeclineRegistrationCommand(candidateId = "candidateId", reason = "we need more"))
        .expectEvents(RegistrationDeclinedEvent(candidateId = "candidateId", reason = "we need more"))
  }
}
