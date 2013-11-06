package dk.dtu.ws.hotelservice.domain;

public class Address {
    
    private final String street;
    private final String houseNo;
    private final String city;
    private final String zip;

    public Address(String street, String houseNo, String city, String zip) {
        this.street = street;
        this.houseNo = houseNo;
        this.city = city;
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return "Denmark";
    }

    /* generated */
    
    @Override
    public String toString() {
        return "Address{" + "street=" + street + ", houseNo=" + houseNo + ", city=" + city + ", zip=" + zip + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.street != null ? this.street.hashCode() : 0);
        hash = 53 * hash + (this.houseNo != null ? this.houseNo.hashCode() : 0);
        hash = 53 * hash + (this.city != null ? this.city.hashCode() : 0);
        hash = 53 * hash + (this.zip != null ? this.zip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if ((this.street == null) ? (other.street != null) : !this.street.equals(other.street)) {
            return false;
        }
        if ((this.houseNo == null) ? (other.houseNo != null) : !this.houseNo.equals(other.houseNo)) {
            return false;
        }
        if ((this.city == null) ? (other.city != null) : !this.city.equals(other.city)) {
            return false;
        }
        if ((this.zip == null) ? (other.zip != null) : !this.zip.equals(other.zip)) {
            return false;
        }
        return true;
    }
    
}    
    