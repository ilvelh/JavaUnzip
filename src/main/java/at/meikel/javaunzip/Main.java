package at.meikel.javaunzip;

import com.Ostermiller.util.CmdLn;
import com.Ostermiller.util.CmdLnListener;
import com.Ostermiller.util.CmdLnOption;
import com.Ostermiller.util.CmdLnResult;

import java.io.File;

public class Main {

    private boolean printHelp;
    private String sourceFileInput;
    private String targetDirInput;
    private File sourceFile;
    private File targetDir;
    private boolean dropTarget;

    public static void main(String[] args) {
        if (args.length == 0) {
            args = new String[]{
                    "--drop-target",
                    "--source",
                    "C:\\Users\\becker\\IdeaProjects\\Fussball-Tippspiel\\target\\at.meikel.fts-src.zip",
                    "--target",
                    "C:\\Temp\\tralala",
            };
        }

        for (String arg : args) {
            System.out.println(arg);
        }

        Main main = new Main();
        main.parseArgs(args);
        main.validate();
        JavaUnzip.unzip(main.getSourceFile(), main.getTargetDir(), main.isDropTarget());
    }

    public Main() {
        targetDirInput = ".";
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
                                sourceFileInput = result.getArgument();
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("target", 't').setRequiredArgument().setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                targetDirInput = result.getArgument();
                            }
                        }
                )
        );
        cmdLn.addOption(
                new CmdLnOption("drop-target", 'd').setDescription("no description available").setListener(
                        new CmdLnListener() {
                            public void found(CmdLnResult result) {
                                dropTarget = true;
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

    private void validate() {
        sourceFile = new File(sourceFileInput);
        targetDir = new File(targetDirInput);
    }

    private File getSourceFile() {
        return sourceFile;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public boolean isDropTarget() {
        return dropTarget;
    }
}
