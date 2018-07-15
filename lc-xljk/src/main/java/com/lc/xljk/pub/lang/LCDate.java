package com.lc.xljk.pub.lang;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LCDate implements java.io.Serializable, Comparable {
	static Logger logger = LoggerFactory.getLogger(LCDate.class);
	static final long serialVersionUID = -1037968151602108293L;

	private String value = null;

	private static final long millisPerDay = 24 * 60 * 60 * 1000;

	private static final int LRUSIZE = 500;

	private static class LRUMap<K, V> extends LinkedHashMap<K, V> {

		private static final long serialVersionUID = 1L;

		public LRUMap(int initSize) {
			super(initSize, 1f, true);
		}

		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			if (size() > LRUSIZE)
				return true;
			else
				return false;
		}
	}

	// 512 is fix the bug of jdk to avoid transfer
	private final static Map<Object, LCDate> allUsedDate = new LRUMap<Object, LCDate>(
			512);

	private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	private transient Long currentLong = null;

	public LCDate() {
		this(new Date());
	}

	public LCDate(long m) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone
				.getTimeZone("Asia/Shanghai"));
		cal.setTimeInMillis(m);
		value = toDateString(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	public LCDate(String strDate) {
		this(strDate, true);
	}

	public LCDate(String strDate, boolean isParse) {
		if (isParse) {
			value = internalParse(strDate);
		} else {
			if (strDate == null || strDate.trim().length() != 10) {
				throw new IllegalArgumentException("invalid UFDate:" + strDate);
			}
			value = strDate.trim();
		}
	}
	public LCDate(java.sql.Date date) {
		this((java.util.Date) date);
	}

	public LCDate(java.util.Date date) {
		value = toDateString(date);
	}
	public boolean after(LCDate when) {
		return this.compareTo(when) > 0;
	}
	public boolean before(LCDate when) {
		return this.compareTo(when) < 0;
	}
	public Object clone() {
		return new LCDate(value);
	}
	public int compareTo(LCDate when) {
		return compareTo(when.getMillis());
	}
	private int compareTo(Long whenLong) {
		long retl = this.getMillis() - whenLong;
		if (retl == 0)
			return 0;
		else
			return retl > 0 ? 1 : -1;
	}
	public boolean equals(Object o) {
		if ((o != null) && (o instanceof LCDate)) {
			return this.getMillis() == ((LCDate) o).getMillis();
		}
		return false;
	}

	public static LCDate getDate(long d) {

		return getDate(d, false);
	}

	public static LCDate getDate(String strDate) {
		if (strDate == null || strDate.trim().length() == 0)
			return null;
		return getDate(strDate, true);
	}

	public static LCDate getDate(Date date) {
		String strDate = toDateString(date);
		return getDate(strDate, false);
	}

	public static LCDate getDate(String date, boolean check) {
		return getDate((Object) date, check);
	}

	public static LCDate getDate(Long date) {
		return getDate((Object) date, false);
	}

	private static LCDate getDate(Object date, boolean check) {
		if (date instanceof Long || date instanceof String) {
			if (rwl.readLock().tryLock()) {
				try {
					LCDate o = null;
					try{
						o = (LCDate) allUsedDate.get(date);
					} catch (Exception e) {
						logger.debug("ufdate error:"+e.getMessage(),e);
					}
					if (o == null) {
						LCDate n = toUFDate(date, check);
						rwl.readLock().unlock();
						rwl.writeLock().lock();
						try {
							try{
								o = allUsedDate.get(date);
							} catch (Exception e) {
								logger.debug("ufdate error:"+e.getMessage(),e);
							}
							if (o == null) {
								o = n;
								allUsedDate.put(date, o);
							}
						} finally {
							rwl.readLock().lock();
							rwl.writeLock().unlock();
						}
					}
					return o;
				} finally {
					rwl.readLock().unlock();
				}
			} else {
				return toUFDate(date, check);
			}
		} else {
			throw new IllegalArgumentException(
					"expect long or string parameter as the first parameter");
		}
	}

	private static LCDate toUFDate(Object date, boolean check) {
		if (date instanceof String)
			return new LCDate((String) date, check);
		else
			return new LCDate((Long) date);
	}

	public LCDate getDateAfter(int days) {
		long l = getMillis() + millisPerDay * days;
		Date date = new Date(l);
		return getDate(date);

	}
	public LCDate getDateBefore(int days) {
		return getDateAfter(-days);
	}
	public int getDay() {
		return Integer.parseInt(value.substring(8, 10));
	}

	public int getDaysAfter(LCDate when) {
		int days = 0;
		if (when != null) {
			days = (int) ((this.getMillis() - when.getMillis()) / millisPerDay);
		}
		return days;
	}

	public static int getDaysBetween(LCDate begin, LCDate end) {
		int days = 0;
		if (begin != null && end != null) {
			days = (int) ((end.getMillis() - begin.getMillis()) / millisPerDay);
		}
		return days;
	}

	public int getDaysMonth() {
		return getDaysMonth(getYear(), getMonth());
	}

	public static int getDaysMonth(int year, int month) {
		switch (month) {
		case 1:
			return 31;
		case 2:
			if (isLeapYear(year))
				return 29;
			else
				return 28;
		case 3:
			return 31;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 30;
		case 7:
			return 31;
		case 8:
			return 31;
		case 9:
			return 30;
		case 10:
			return 31;
		case 11:
			return 30;
		case 12:
			return 31;
		default:
			return 30;
		}
	}

	public String getEnMonth() {
		switch (getMonth()) {
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dec";
		}
		return null;
	}

	public String getEnWeek() {
		switch (getWeek()) {
		case 0:
			return "Sun";
		case 1:
			return "Mon";
		case 2:
			return "Tue";
		case 3:
			return "Wed";
		case 4:
			return "Thu";
		case 5:
			return "Fri";
		case 6:
			return "Sat";
		}
		return null;
	}

	public int getMonth() {
		return Integer.parseInt(value.substring(5, 7));
	}

	public String getStrDay() {
		return value.substring(8, 10);
	}

	public String getStrMonth() {
		return value.substring(5, 7);
	}

	public int getWeek() {
		int days = getDaysAfter(new LCDate("1980-01-06"));
		int week = days % 7;
		if (week < 0)
			week += 7;
		return week;
	}

	public int getYear() {
		return Integer.parseInt(value.substring(0, 4));
	}

	public boolean isLeapYear() {
		return isLeapYear(getYear());
	}

	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0))
			return true;
		else
			return false;
	}

	public String toString() {
		return value == null ? "" : value;
	}

	public int compareTo(Object o) {
		if (o instanceof LCDate)
			return compareTo((LCDate) o);
		else if (o instanceof LCDateTime)
			return compareTo(((LCDateTime) o).getMillis());
		else
			throw new IllegalArgumentException();
	}

	public long getMillis() {
		if (currentLong == null) {
			try {
			GregorianCalendar cal = new GregorianCalendar(TimeZone
					.getTimeZone("Asia/Shanghai"));
			cal.set(Calendar.YEAR, getYear());
			cal.set(Calendar.MONTH, getMonth() - 1);
			cal.set(Calendar.DAY_OF_MONTH, getDay());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			currentLong = cal.getTimeInMillis();
			} catch (Exception e) {
				logger.debug("ufdate error:"+e.getMessage(),e);
			}

		}
		return currentLong;

	}

	public int getWeekOfYear() {
		GregorianCalendar calendar = new GregorianCalendar(getYear(),
				getMonth(), getDay());
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	public Date toDate() {
		return new Date(getMillis());
	}

	@Override
	public int hashCode() {
		return value == null ? 17 : value.hashCode();
	}

	private static String internalParse(String sDate) {

		if (sDate == null)
			throw new IllegalArgumentException("invalid UFDate: " + sDate);

		sDate = sDate.trim();
		String[] tokens = new String[3];

		StringTokenizer st = new StringTokenizer(sDate, "-/.");

		if (st.countTokens() != 3) {
			throw new IllegalArgumentException("invalid UFDate: " + sDate);
		}

		int i = 0;
		while (st.hasMoreTokens()) {
			tokens[i++] = st.nextToken().trim();
		}

		try {
			int year = Integer.parseInt(tokens[0]);
			int month = Integer.parseInt(tokens[1]);
			if (month < 1 || month > 12)
				throw new IllegalArgumentException("invalid UFDate: " + sDate);
			int day = Integer.parseInt(tokens[2]);

			int MONTH_LENGTH[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
					31 };
			int LEAP_MONTH_LENGTH[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31,
					30, 31 };
			int daymax = isLeapYear(year) ? LEAP_MONTH_LENGTH[month - 1]
					: MONTH_LENGTH[month - 1];

			if (day < 1 || day > daymax)
				throw new IllegalArgumentException("invalid ufdate: " + sDate);

			String strYear = tokens[0];
			for (int j = strYear.length(); j < 4; j++) {
				if (j == 3) {
					strYear = "2" + strYear;
				} else {
					strYear = "0" + strYear;
				}
			}

			String strMonth = String.valueOf(month);
			if (strMonth.length() < 2)
				strMonth = "0" + strMonth;
			String strDay = String.valueOf(day);
			if (strDay.length() < 2)
				strDay = "0" + strDay;
			return strYear + "-" + strMonth + "-" + strDay;
		} catch (Throwable thr) {
			if (thr instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) thr;
			} else {
				throw new IllegalArgumentException("invalid ufdate: " + sDate);
			}
		}

	}

	private static String toDateString(int year, int month, int day) {
		String strYear = String.valueOf(year);
		for (int j = strYear.length(); j < 4; j++)
			strYear = "0" + strYear;
		String strMonth = String.valueOf(month);
		if (strMonth.length() < 2)
			strMonth = "0" + strMonth;
		String strDay = String.valueOf(day);
		if (strDay.length() < 2)
			strDay = "0" + strDay;
		return strYear + "-" + strMonth + "-" + strDay;

	}

	private static String toDateString(Date dt) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
		cal.setTime(dt);
		return toDateString(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	public static String getValidUFDateString(String sDate) {
		return internalParse(sDate);
	}
	public static boolean isAllowDate(String strDate) {
		try {
			internalParse(strDate);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}