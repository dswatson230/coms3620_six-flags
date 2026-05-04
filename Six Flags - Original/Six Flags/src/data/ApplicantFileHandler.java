package data;

import interfaces.ApplicantFileHandlerInterface;
import models.Applicant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ApplicantFileHandler implements ApplicantFileHandlerInterface {
    private final Path applicantFilePath;

    public ApplicantFileHandler(String applicantFilePath) {
        this.applicantFilePath = Paths.get(applicantFilePath);
    }

    @Override
    public Applicant findApplicantById(String applicantId) throws IOException {
        for (Applicant applicant : readAllApplicants()) {
            if (applicant.getApplicantId().equals(applicantId)) {
                return applicant;
            }
        }
        return null;
    }

    @Override
    public boolean updateApplicantStatus(String applicantId, String applicantStatus) throws IOException {
        List<Applicant> applicants = readAllApplicants();
        boolean updated = false;

        for (int i = 0; i < applicants.size(); i++) {
            Applicant applicant = applicants.get(i);
            if (applicant.getApplicantId().equals(applicantId)) {
                applicants.set(i, applicant.withStatus(applicantStatus));
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllApplicants(applicants);
        }
        return updated;
    }

    @Override
    public List<Applicant> readAllApplicants() throws IOException {
        List<Applicant> applicants = new ArrayList<>();
        if (Files.notExists(applicantFilePath)) return applicants;

        for (String line : Files.readAllLines(applicantFilePath)) {
            if (line.trim().isEmpty()) continue;
            Applicant applicant = parseApplicantRecord(line);
            if (applicant != null) applicants.add(applicant);
        }
        return applicants;
    }

    private Applicant parseApplicantRecord(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 2) {
            return new Applicant(parts[0].trim(), parts[1].trim());
        }
        if (parts.length == 3) {
            return new Applicant(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        return null;
    }

    private void writeAllApplicants(List<Applicant> applicants) throws IOException {
        if (applicantFilePath.getParent() != null) {
            Files.createDirectories(applicantFilePath.getParent());
        }
        StringBuilder contents = new StringBuilder();
        for (Applicant applicant : applicants) {
            contents.append(formatApplicantRecord(applicant)).append(System.lineSeparator());
        }
        Files.writeString(applicantFilePath, contents.toString(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String formatApplicantRecord(Applicant applicant) {
        return applicant.getApplicantId() + " | " + applicant.getApplicantName() + " | "
             + applicant.getApplicantStatus().toUpperCase();
    }
}
