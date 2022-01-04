package easycriteria;

import java.math.BigDecimal;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="large_project_tbl")
@AttributeOverride(name="name", column=@Column(name="PROJECT_NAME"))
public class LargeProject extends Project {
	
	private BigDecimal budget;

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

}
