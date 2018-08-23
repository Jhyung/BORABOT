/*
 * Mykhailo Pietielin 2017.
 * https://github.com/RoanDev
 * roanworkbox@gmail.com
 * GPL c:
 */

/*
 * Mykhailo Pietielin 2017.
 * https://github.com/RoanDev
 * roanworkbox@gmail.com
 * GPL c:
 */

package exchangeAPI;


import hitbtc.api.MarketAPI;
import hitbtc.api.PaymentAPI;
import hitbtc.api.TradeAPI;
import hitbtc.exceptions.HitBTCAccessDenied;
import hitbtc.internal.APIMode;

public class HitBTC {
    private String apiKey, apiSecret;
    private MarketAPI marketAPI;
    private PaymentAPI paymentAPI;
    private TradeAPI tradeAPI;

    public HitBTC(APIMode apiAccessMode) {
        this.marketAPI = new MarketAPI(this.generateHttpEndpointFor(apiAccessMode));
    }

    public HitBTC(APIMode apiAccessMode, String API, String Secret) {
        this.apiKey = API;
        this.apiSecret = Secret;
        this.marketAPI = new MarketAPI(this.generateHttpEndpointFor(apiAccessMode));
        this.paymentAPI = new PaymentAPI(apiKey, apiSecret, this.generateHttpEndpointFor(apiAccessMode));
        this.tradeAPI = new TradeAPI(apiKey, apiSecret, this.generateHttpEndpointFor(apiAccessMode));
    }

    private String generateHttpEndpointFor(APIMode mode) {
        return mode.equals(APIMode.DEMO) ? "http://demo-api.hitbtc.com" : "http://api.hitbtc.com";
    }

    public MarketAPI marketAPI() {
        return marketAPI;
    }

    public PaymentAPI paymentAPI() throws HitBTCAccessDenied {
        if (paymentAPI == null) {
            throw new HitBTCAccessDenied("API keys not provided");
        }
        return paymentAPI;
    }

    public TradeAPI tradeAPI() throws HitBTCAccessDenied {
        if (tradeAPI == null) {
            throw new HitBTCAccessDenied("API keys not provided");
        }
        return tradeAPI;
    }
}
