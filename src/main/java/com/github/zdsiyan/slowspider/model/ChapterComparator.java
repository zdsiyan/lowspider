package com.github.zdsiyan.slowspider.model;

import java.util.Comparator;

public class ChapterComparator implements Comparator<Chapter>{

	@Override
	public int compare(Chapter o1, Chapter o2) {
		if(o1==null || o1.sort==null)
			return 1;
		if(o2==null || o2.sort==null)
			return -1;
		return o1.sort.compareTo(o2.sort);
	}

}
