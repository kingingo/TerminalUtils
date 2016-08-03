package dev.wolveringer.terminal.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;

@RequiredArgsConstructor
public class TerminalTable {
	public static enum Align {
		LEFT,
		RIGHT,
		CENTER;
	}
	
	@AllArgsConstructor
	@Getter
	public static class TerminalColumn {
		private String name;
		private Align align;
	}
	
	public static class TerminalRow {
		@Getter
		private List<String>[] columns;
		
		public TerminalRow(String...strings) {
			this(strings.length);
			for(int i = 0;i<strings.length;i++)
				setText(i, strings[i]);
		}
		
		public TerminalRow(int size) {
			columns = new List[size];
			for(int i = 0;i<size;i++)
				columns[i] = new ArrayList<>();
		}
		
		public void setText(int column,String message){
			columns[column].clear();
			for(String m : message.split("\n"))
				columns[column].add(m);
		}
	}
	
	private final TerminalColumn[] column;
	private ArrayList<TerminalRow> rows = new ArrayList<>();
	
	public void addRow(String...strings){
		if(strings.length != column.length)
			throw new RuntimeException("Row size isnt equals to column size!");
		rows.add(new TerminalRow(strings));
	}
	
	public void addRow(TerminalRow row){
		if(row.columns.length != column.length)
			throw new RuntimeException("Row size isnt equals to column size!");
		rows.add(row);
	}
	
	public void printTable(){
		for(String s : buildLines())
			System.out.println(s);
	}
	
	private static final String columnSeperator = "§r§7 | §r";
	
	public List<String> buildLines(){
		ArrayList<String> lines = new ArrayList<>();
		
		int[] columnSize = new int[column.length];
		for(int column = 0;column < this.column.length; column++){
			columnSize[column] = getColumnMaxWith(column);	
		}
		
		/*
		int tableSize = 0;
		for(int i : columnSize)
			tableSize+=i+columnSeperator.length();
		tableSize-=columnSeperator.length();
		*/
		String line = "";
		
		for(int index = 0;index < this.column.length; index++)
			line += columnSeperator+formatString(column[index].name, column[index].align, columnSize[index]);
		lines.add(line.substring(columnSeperator.length()));
		
		line = "";
		for(int i : columnSize){
			line+=columnSeperator.replaceAll(" ", "-")+"§7"+StringUtils.leftPad("", i, '-');
		}
		lines.add(line.substring(columnSeperator.length()));
		
		for(TerminalRow row : rows){
			line = "";
			int maxLines = 1;
			for(List<String> column : row.columns)
				if(column.size() > maxLines)
					maxLines = column.size();
			for(int i = 0;i<maxLines;i++){
				line = "";
				for(int index = 0;index < row.columns.length; index++){
					line += columnSeperator+formatString(i < row.columns[index].size() ? row.columns[index].get(i) : "", column[index].align, columnSize[index]);
				}
				lines.add(line.substring(columnSeperator.length()));
			}
		}
		return lines;
	}
	
	private String formatString(String in, Align align, int size){
		int absuluteSize = (in.length()-getStringLength(in))+size;
		switch (align) {
		case LEFT:
			return StringUtils.leftPad(in, absuluteSize);
		case RIGHT:
			return StringUtils.rightPad(in, absuluteSize);
		default:
			return StringUtils.center(in, absuluteSize);
		}
	}
	
	private int getColumnMaxWith(int column){
		int currentLength = 0;
		
		for(TerminalRow row : rows)
			for(String m : row.columns[column])
				if(getStringLength(m) > currentLength)
					currentLength = getStringLength(m);
		if(getStringLength(this.column[column].getName()) > currentLength)
			currentLength = getStringLength(this.column[column].getName());
		
		return currentLength;
	}
	
	private int getStringLength(String in){
		if(in == null)
			return 4;
		return ChatColor.stripColor(in).length();
	}
}
