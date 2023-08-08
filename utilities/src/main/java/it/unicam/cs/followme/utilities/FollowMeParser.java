package it.unicam.cs.followme.utilities;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Instances of this class are used to parse a program from an external source.
 */
public final class FollowMeParser {
    private int counter = 0;  //contatore di linee di comando

    private int errors_counter=0;
    private final FollowMeParserHandler handler; //handler dei comandi

    private final FollowMeShapeChecker checker;  //checker delle shape

    public FollowMeParser(FollowMeParserHandler handler, FollowMeShapeChecker checker) {  //constructor con nuovo checker
        this.handler = handler;
        this.checker = checker;
    }

    public FollowMeParser(FollowMeParserHandler handler) {  //constructor con default checker
        this(handler, FollowMeShapeChecker.DEFAULT_CHECKER);
    }

    public FollowMeParser() {  //constructor con default checker  //HO AGGIUNTO QUESTO COSTRUTTORE PER FARE DEI TEST SULLA CREAZIONE DELLE SHAPES
        this(null, FollowMeShapeChecker.DEFAULT_CHECKER);
    }  //COSTRUTTORE DEL PARSER SENZA HANDLER SOLO CON SHAPE CHEKER DI DEFAULT



    public synchronized void parseRobotProgram(File sourceFile) throws Exception {  //parser di una serie di comandi provenienti da file
        parseRobotProgram(sourceFile.toPath()); //??
    }

    public synchronized void parseRobotProgram(Path path) throws Exception {   //parser di una serie di comandi provenienti da path
        parseRobotProgram(Files.readAllLines(path));
    }


    public synchronized void parseRobotProgram(String code) throws Exception {  //parser di una serie di comandi provenienti da una lunga stringa
        parseRobotProgram(List.of(code.split("\n")));
    }

    public synchronized List<ShapeData> parseEnvironment(File file) throws IOException, FollowMeParserException {
        return parseEnvironment(file.toPath());
    }

    public synchronized List<ShapeData> parseEnvironment(String data) throws FollowMeParserException {
        return parseEnvironment(List.of(data.split("\n")));
    }

    public synchronized List<ShapeData> parseEnvironment(Path path) throws IOException, FollowMeParserException {
        return parseEnvironment(Files.readAllLines(path));
    }

    private List<ShapeData> parseEnvironment(List<String> lines) throws FollowMeParserException {
        this.counter = 0;
        List<ShapeData> data = new LinkedList<>();
        for (String line: lines) {
            this.counter++;
            String[] elements = line.trim().toUpperCase().split(" ");
            if (checker.checkParameters(elements)) {
                data.add(ShapeData.fromString(elements));
            } else {
                throwSyntaxErrorException();
            }
        }
        return data;
    }

    private void parseRobotProgram(List<String> lines) throws Exception {  //parser di una List di stringhe contenenti i comandi per i robot
        this.counter = 0;

        for (String line: lines) {
            this.handleLine(line.trim().toUpperCase());
        }

    }


    private void handleLine(String line) throws Exception {        //fa il parsing di un comando che gli arriva da una fonte (file, long string, path )
        counter++;
        if (!line.isBlank()) {
            line.trim().toUpperCase();
            selectAndCallHandlerMethod(handler, line);
        }
    }


    private void selectAndCallHandlerMethod(FollowMeParserHandler handler, String line) throws Exception {
        Optional<RobotCommand> oCommand = RobotCommand.selectCommand(line);
        if (oCommand.isEmpty()) {
            throw new FollowMeParserException(FollowMeParserUtil.unknownCommandMessage(this.counter));
        }
        callHandlerMethod(handler, oCommand.get(), line);
    }

    private void callHandlerMethod(FollowMeParserHandler handler, RobotCommand robotCommand, String line) throws Exception {
        String[] elements = line.split(" ");
        switch (robotCommand) {
            case MOVE       -> callMoveMethods(handler, elements);
            case FOLLOW     -> callFollowMethod(handler, elements);


            case SIGNAL     -> callSignalMethod(handler, elements);
            case UNSIGNAL   -> callUnSignalMethod(handler, elements);


            case STOP       -> callStopMethod(handler, elements);
            case CONTINUE   -> callContinueMethod(handler, elements);


        }
    }




    private void callContinueMethod(FollowMeParserHandler handler, String[] elements) throws FollowMeParserException {
        if (elements.length == 2) {
            try {
                handler.continueCommand(Integer.parseInt(elements[1]));
            } catch (NumberFormatException e) {
                throwSyntaxErrorException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callStopMethod(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length == 1) {
            handler.stopCommand();
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callFollowMethod(FollowMeParserHandler handler, String[] elements) throws FollowMeParserException {
        if (elements.length == 4) {
            try {
                handler.followCommand(elements[1], toDoubleArray(2, elements));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throwSyntaxErrorException();
        }

    }


    private void callUnSignalMethod(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length == 2) {
            handler.unsignalCommand(elements[1]);
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callSignalMethod(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length == 2) {
            handler.signalCommand(elements[1]);
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveMethods(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length > 1) {
            if (elements[1].equals("RANDOM")) {
                callMoveRandomMethod(handler, elements);
            } else {
                callMoveMethod(handler, elements);
            }
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveMethod(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length == 4) {
            handler.moveCommand(toDoubleArray(1,elements));
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveRandomMethod(FollowMeParserHandler handler, String[] elements) throws Exception {
        if (elements.length == 7) {
            handler.moveRandomCommand(toDoubleArray(2,elements));
        } else {
            throwSyntaxErrorException();
        }
    }

    private double[] toDoubleArray(int from, String[] elements) throws FollowMeParserException {
        try {
            double[] result = new double[elements.length-from];
            for(int i=0;i<result.length;i++) {
                result[i] = Double.parseDouble(elements[from+i]);
            }
            return result;
        } catch (NumberFormatException e) {
            throwSyntaxErrorException();
        }
        return null;
    }

    private void throwSyntaxErrorException() throws FollowMeParserException {
        this.errors_counter++;
        throw new FollowMeParserException(String.format("Syntax error at line %d", counter));
    }


}
