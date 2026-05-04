package interfaces;

import models.JobOpening;

import java.io.IOException;
import java.util.List;

public interface JobOpeningFileHandlerInterface {
    JobOpening findJobOpeningById(String jobOpeningId) throws IOException;
    boolean updateJobOpeningStatus(String jobOpeningId, String openingStatus) throws IOException;
    List<JobOpening> readAllJobOpenings() throws IOException;
}
