package data;

import interfaces.JobOpeningFileHandlerInterface;
import models.JobOpening;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class JobOpeningFileHandler implements JobOpeningFileHandlerInterface {
    private final Path jobOpeningFilePath;

    public JobOpeningFileHandler(String jobOpeningFilePath) {
        this.jobOpeningFilePath = Paths.get(jobOpeningFilePath);
    }

    @Override
    public JobOpening findJobOpeningById(String jobOpeningId) throws IOException {
        for (JobOpening jobOpening : readAllJobOpenings()) {
            if (jobOpening.getJobOpeningId().equals(jobOpeningId)) {
                return jobOpening;
            }
        }
        return null;
    }

    @Override
    public boolean updateJobOpeningStatus(String jobOpeningId, String openingStatus) throws IOException {
        List<JobOpening> jobOpenings = readAllJobOpenings();
        boolean updated = false;

        for (int i = 0; i < jobOpenings.size(); i++) {
            JobOpening jobOpening = jobOpenings.get(i);
            if (jobOpening.getJobOpeningId().equals(jobOpeningId)) {
                jobOpenings.set(i, jobOpening.withOpeningStatus(openingStatus));
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllJobOpenings(jobOpenings);
        }
        return updated;
    }

    @Override
    public List<JobOpening> readAllJobOpenings() throws IOException {
        List<JobOpening> jobOpenings = new ArrayList<>();
        if (Files.notExists(jobOpeningFilePath)) return jobOpenings;

        for (String line : Files.readAllLines(jobOpeningFilePath)) {
            if (line.trim().isEmpty()) continue;
            JobOpening jobOpening = parseJobOpeningRecord(line);
            if (jobOpening != null) jobOpenings.add(jobOpening);
        }
        return jobOpenings;
    }

    private JobOpening parseJobOpeningRecord(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 5) return null;
        return new JobOpening(
            parts[0].trim(), parts[1].trim(), parts[2].trim(),
            parts[3].trim(), parts[4].trim()
        );
    }

    private void writeAllJobOpenings(List<JobOpening> jobOpenings) throws IOException {
        if (jobOpeningFilePath.getParent() != null) {
            Files.createDirectories(jobOpeningFilePath.getParent());
        }
        StringBuilder contents = new StringBuilder();
        for (JobOpening jobOpening : jobOpenings) {
            contents.append(formatJobOpeningRecord(jobOpening)).append(System.lineSeparator());
        }
        Files.writeString(jobOpeningFilePath, contents.toString(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String formatJobOpeningRecord(JobOpening jobOpening) {
        return jobOpening.getJobOpeningId() + " | " + jobOpening.getJobTitle() + " | "
             + jobOpening.getDepartmentName() + " | "
             + jobOpening.getApprovalStatus().toUpperCase() + " | "
             + jobOpening.getOpeningStatus().toUpperCase();
    }
}
