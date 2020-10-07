package eu.opertusmundi.cli.command;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import eu.opertusmundi.cli.SubCommand;

@Component("help")
public class HelpCommand implements SubCommand {

    @Autowired
    ApplicationContext applicationContext;

    @Value("${git.build.version}")
    String buildVersion;

    @Value("${git.commit.id.abbrev}")
    String commitId;

    @Value("${git.build.time}")
    String buildTimestamp;

    private SubCommand subcommandByName(String name) {
        SubCommand c;
        try {
            c = this.applicationContext.getBean(name, SubCommand.class);
        } catch (final BeansException ex) {
            c = null;
        }
        return c;
    }

    private Map<String, SubCommand> subcommands() {
        return this.applicationContext.getBeansOfType(SubCommand.class);
    }

    @Override
    public void run(Map<String, List<String>> args) {
        System.out.printf("\n[OpertusMundi CLI] %s Commit: %s Build: %s\n\n", this.buildVersion, this.commitId, this.buildTimestamp);

        System.out.println("The following (sub)commands are available:");

        for (final Entry<String, SubCommand> p : this.subcommands().entrySet()) {
            final SubCommand c = p.getValue();
            System.out.printf(" * %-12.10s %s%n", p.getKey(), c.getDescription());
        }
    }

    @Override
    public void run(String subcommandName, Map<String, List<String>> options) {
        // Print help on a given subcommand
        final SubCommand c = subcommandName.equals("help") ? this : this.subcommandByName(subcommandName);
        if (c == null) {
            System.out.printf("No such command: %s%n", subcommandName);
        } else {
            System.out.println(c.getSummary());
        }
    }

    @Override
    public void run(String subcommandName, String a2, Map<String, List<String>> options) {
        // ignore any extra non-option arguments, just print help on subcommand
        this.run(subcommandName, options);
    }

    @Override
    public void run(String subcommandName, String a2, String a3, Map<String, List<String>> options) {
        // ignore any extra non-option arguments, just print help on subcommand
        this.run(subcommandName, options);
    }

    @Override
    public String getSummary() {
        return "help [<subcommand-name>]";
    }

    @Override
    public String getDescription() {
        return "List available commands or ask help on a specific command";
    }
}
