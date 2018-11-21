import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The class parse the vm file to lines instructions without comments and empty lines
 */
public class Parser {

    private List<String> lines;
    private int destLineNo = -1;

    Parser(String path) {
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {

        }
    }

    void reset()
	{
		destLineNo = -1;
	}

    /**
     * return true if there are more commands
     */
    public boolean hasMoreCommands() {
        for(int i = destLineNo+1; i <lines.size();i++)
		{
			int comment = lines.get(i).indexOf("//");
			if(comment>=0)
				lines.set(i,lines.get(i).substring(0,comment));

			lines.set(i,lines.get(i).trim().replaceAll("\\s"," "));

			if(!lines.get(i).equals(""))
			{
				return true;
			}
		}

		//we didn't find another COMMAND
		return false;
    }

    /**
     * get the next instruction if there are more commands
     */
    public void advance()
    {
        destLineNo++;
        if(destLineNo<lines.size())
        {
            if(lines.get(destLineNo).equals(""))
            {
                advance();
            }
        }
    }

    /**
     * return command type
     */
    public CommandType commandType(){
        switch(lines.get(destLineNo).split(" ")[0]){
            case "push":
                return CommandType.C_PUSH;
            case "pop":
                return CommandType.C_POP;
            case "label":
                return CommandType.C_LABEL;
            case "goto":
                return CommandType.C_GOTO;
            case "if-goto":
                return CommandType.C_IF;
            case "function":
                return CommandType.C_FUNCTION;
            case "call":
                return CommandType.C_CALL;
            case "return":
                return CommandType.C_RETURN;
        }
        return CommandType.C_ARITHMETIC;
    }


    public String arg1(){
        if(commandType()==CommandType.C_ARITHMETIC)
            return lines.get(destLineNo);

        return lines.get(destLineNo).split(" ")[1];
    }

    public int arg2(){
        return Integer.parseInt(lines.get(destLineNo).split(" ")[2]);
    }


}
