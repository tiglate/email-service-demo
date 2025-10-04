package ludo.mentis.aciem.auctoritas.dev;

import ludo.mentis.aciem.auctoritas.domain.Software;
import ludo.mentis.aciem.auctoritas.repos.SoftwareRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SoftwaresLoader implements DataLoaderCommand {

    private final SoftwareRepository repository;

    public SoftwaresLoader(SoftwareRepository repository) {
        this.repository = repository;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return "Applications";
    }

    @Override
    public boolean canItRun() {
        return repository.count() == 0;
    }

    @Override
    public int run() {
        var apps = List.of(
                new Software("CRE", "Core Banking System"),
                new Software("CRM", "Customer Relationship Manager"),
                new Software("LND", "Loan Origination Platform"),
                new Software("TRD", "Treasury & Trading System"),
                new Software("FXM", "Foreign Exchange Management"),
                new Software("RPT", "Regulatory Reporting Suite"),
                new Software("MOB", "Mobile Banking App"),
                new Software("IBK", "Internet Banking Portal"),
                new Software("KYC", "Know Your Customer Platform"),
                new Software("RIS", "Risk Management & Compliance Tool")
        );
        repository.saveAll(apps);
        return apps.size();
    }
}
