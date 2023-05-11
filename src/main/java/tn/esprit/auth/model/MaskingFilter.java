package tn.esprit.auth.model;

public class MaskingFilter {
	
	 public MaskingFilter() {
     }

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return true;
		return (int) obj == -1;
	}

	 
	 
}
