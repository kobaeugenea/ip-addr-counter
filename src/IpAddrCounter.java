import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

public class IpAddrCounter {

	public IpAddrCounter() {
		validateAddresses = false;
	}

	/**
	 * @param validateAddresses use to validate correctness of IP addresses with regex. It slows down  processing a lot.
	 */
	public IpAddrCounter(boolean validateAddresses) {
		this.validateAddresses = validateAddresses;
	}

	private final static long MAX_IP = 0x00000000ffffffffL;
	private final static String IP_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

	private final boolean validateAddresses;

	private final BitSet[] countedAddresses = new BitSet[] {
			new BitSet(0x40000000), // 0x00000000 - 0x3FFFFFFF
			new BitSet(0x40000000), // 0x40000000 - 0x7FFFFFFF
			new BitSet(0x40000000), // 0x80000000 - 0xBFFFFFFF
			new BitSet(0x40000000)  // 0x8C000000 - 0xFFFFFFFF
	};

	private long uniqueAddressesAmount = 0;
	/**
	 * Count unique ip addresses in file
	 * @param file file to process
	 * @throws IOException any exceptions related to file
	 */
	public void count(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();

		while (line != null) {
			if(validateAddresses && !line.matches(IP_REGEX)) {
				throw new IllegalArgumentException("File contains invalid format if IP address");
			}

			long ip = ipStringToLongIp(line);
			int firstTwoIpBits = (int) ((ip & 0b11000000000000000000000000000000) >> 30);
			int restBits =       (int)  (ip & 0b00111111111111111111111111111111);

			count(countedAddresses[firstTwoIpBits], restBits);

			line = reader.readLine();
		}
	}

	public long getUniqueAddressesAmount() {
		return uniqueAddressesAmount;
	}

	/**
	 * Count ip address if it wasn't counted yet
	 * @param countedAddresses counted addresses storage that will be checked
	 * @param ip ip in integer form
	 */
	private void count(BitSet countedAddresses, int ip) {
		if (!countedAddresses.get(ip)) {
			countedAddresses.set(ip);
			uniqueAddressesAmount++;
		}
	}

	/**
	 * taken from <a href="http://www.java2s.com/example/java-utility-method/ip-address-to-long/convertips2long-string-ip-c017f.html">here</a>
	 */
	private long ipStringToLongIp(String ip) {
		char[] c = ip.toCharArray();
		byte[] b = {0, 0, 0, 0};
		for (int i = 0, j = 0; i < c.length; ) {
			int d = (byte) (c[i] - '0');
			if (c[i++] == '.') {
				++j;
			}
			else {
				if ((d < 0) || (d > 9)) {
					return 0;
				}
				if ((b[j] & 0xff) * 10 + d > 255) {
					return 0;
				}
				b[j] = (byte) (b[j] * 10 + d);
			}
		}
		return MAX_IP & (b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
	}

}
