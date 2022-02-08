package com.youngbin.batch.tasklets;


import com.youngbin.bo.ReservationBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class ProcessReservationTasklet implements Tasklet {
    /**
     * @desc reservation 요청을 처리하여 push 알림을 전송하는 tasklet
     */
    private final ReservationBO reservationBO;

    public ProcessReservationTasklet(ReservationBO reservationBO) { this.reservationBO = reservationBO; }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try {
            reservationBO.reserve();
            log.info("Successfully process reservation.");
        } catch (Exception e) {
            log.info("Failed to process reservation.");
        }
        return RepeatStatus.FINISHED;
    }
}
