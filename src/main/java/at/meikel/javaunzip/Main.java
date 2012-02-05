package at.meikel.javaunzip;

import com.Ostermiller.util.CmdLn;
import com.Ostermiller.util.CmdLnListener;
import com.Ostermiller.util.CmdLnOption;
import com.Ostermiller.util.CmdLnResult;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    private boolean printHelp;
    private JavaUnzip javaUnzip;

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{
                    "--source",
                    "..\\meikel-JavaUnzip-1230dd7.zip",
                    "--target",
                    "..\\xxx",
                    "--drop-target",
                    // "--omit-timestamps",
                    "--use-timestamp",
                    "2012-01-17 18-30",
            };
        }

        for (String arg : args) {
            System.out.println(arg);
        }

        Main main = new Main();
        main.parseArgs(args);
        main.unzip();
    }

    public Main() {
        javaUnzip = new JavaUnzip();
    }

    public void parseArgs(String[] args) {
        CmdLn cmdLn = new CmdLn(args);
        cmdLn.addOption(
                new CmdLnOption("help", 'h').setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                printHelp = true;
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("source", 's').setRequiredArgument().setDescription("name (including path) of zip file to unzip").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                javaUnzip.setSourceFile(new File(result.getArgument()));
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("target", 't').setRequiredArgument().setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                javaUnzip.setTargetDir(new File(result.getArgument()));
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("drop-target", 'd').setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                javaUnzip.setDropTarget(true);
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("update-target", 'u').setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                javaUnzip.setUpdateTarget(true);
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("omit-timestamps", 'o').setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                javaUnzip.setOmitTimestamps(true);
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("use-timestamp").setRequiredArgument().setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                                try {
                                    Date useTimestamp = sdf.parse(result.getArgument());
                                    javaUnzip.setUseTimestamp(Long.valueOf(useTimestamp.getTime()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
        );
        cmdLn.parse();
        if (printHelp) {
            cmdLn.printHelp();
            System.exit(-1);
        }
    }

    private void unzip() {
        javaUnzip.unzip();
    }

}
