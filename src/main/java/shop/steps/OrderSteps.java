package shop.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import shop.aut.Calculator;

public final class OrderSteps {
    
    private Calculator calculator = new Calculator();

    @Given("im Lager sind $Menge T-Shirts")
    public void givenAvailableTees(int amount) {
        this.calculator.setAvailableStock(amount);
    }

    @When("ein Kunde $Anzahl T-Shirts bestellt")
    public void whenCustomerOrders(int quantity) {
        this.calculator.setOrderQuantity(quantity);
    }

    @Then("ist die Bestellung auf Lager")
    public void thenIsInStock() {
        assertThat(this.calculator.isInStock(), equalTo(true));
    }
    
    @Then("kostet ein T-Shirt pro Stück $Preis Euro")
    public void thenPricePerTeeIs(double price) {
        assertThat(this.calculator.getPricePerItem(), equalTo(price));
    }

    @Then("betragen die Versandkosten $Betrag Euro")
    public void thenShippingCostsAre(double value) {
        assertThat(this.calculator.getShippingCosts(), equalTo(value));
    }
    
    @Then("ist der Bestellwert $Betrag Euro")
    public void thenOrderValueIs(double value) {
        assertThat(this.calculator.getOrderValue(), equalTo(value));
    }
    
    @Then("gilt eine Ermässigung von $Rabatt Prozent")
    public void thenDiscountIs(double discount) {
        assertThat(this.calculator.getDiscount(), equalTo(discount));
    }
    
}
