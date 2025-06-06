package ee.jackaltech.conferenceplatform.fixedtime;

import org.mockito.Mockito;

import java.time.Clock;

import static org.mockito.Mockito.when;

public class TestClock {
    public static Clock newInstance() {
        var fixedClock = Mockito.mock(Clock.class);
        reset(fixedClock);
        return fixedClock;
    }

    public static void reset(Clock fixedClock) {
        Mockito.reset(fixedClock);
        when(fixedClock.getZone()).thenAnswer(inv -> Clock.systemDefaultZone().getZone());
        when(fixedClock.instant()).thenAnswer(inv -> Clock.systemDefaultZone().instant());
    }
}
