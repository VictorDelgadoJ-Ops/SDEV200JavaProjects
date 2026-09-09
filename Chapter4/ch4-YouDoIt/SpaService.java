// Bill Ruben
// p.133

public class SpaService {
	private String serviceDescription;
	private double price;

	public SpaService() {
		serviceDescription = "XXX";
		price = 0;
	}
	public SpaService(String desc) {
		serviceDescription = desc;
		price = 0;
	}
	public SpaService(String desc, double pr) {
		serviceDescription = desc;
		price = pr;
	}

	public void setServiceDescription(String service) {
		serviceDescription = service;
	}

	public void setPrice(double servicePrice) {
		price = servicePrice;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public double getPrice() {
		return price;
	}
}
