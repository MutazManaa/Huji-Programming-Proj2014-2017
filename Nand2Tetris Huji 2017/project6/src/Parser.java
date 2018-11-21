import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The class parse the sympolic assembley file to lines instructions without comments and empty lines
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

			lines.set(i,lines.get(i).replaceAll("\\s",""));

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
        switch(lines.get(destLineNo).charAt(0))
        {
            case '@':
                return CommandType.A_COMMAND;
            case '(':
                return CommandType.L_COMMAND;
            default:
                return CommandType.C_COMMAND;
        }
    }


    /**
     * return the sympol of the instruction as string
     */
    public String symbol()
    {
        String line = lines.get(destLineNo);
        if(line.charAt(0)=='(')
            return line.substring(1,line.length()-1);
        return line.substring(1);
    }

    /**
     * return the dest of C-instriction as string
     */
    public String dest()
    {
        String line = lines.get(destLineNo);
        int end = line.indexOf('=');
        if(end<0)
        {
            return "";
        }
        return line.substring(0,end);
    }

    /**
     * return the comp of the C-instruction as string
     */
    public String comp()
    {
        String line = lines.get(destLineNo);
        int start = line.indexOf('=')+1;
        int end =line.indexOf(';');
        if(end<0)
		{
			end = line.length();
		}
        return line.substring(start,end);
    }

    /**
     * return the jump of C-intruction as String
     */
    public String jump()
    {
        String line = lines.get(destLineNo);
        int start = line.indexOf(';')+1;
        if(start<=0)
        {
            return "";
        }
        return line.substring(start,line.length());
    }




}
