import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		IpAddrCounter counter = new IpAddrCounter();
		counter.count(new File("path_to_file"));
		System.out.println(counter.getUniqueAddressesAmount());
	}
}
