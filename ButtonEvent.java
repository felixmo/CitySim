/**
 * ButtonEvent
 * CitySim
 * v0.1
 * 
 * Created by Felix Mo on 05-01-2012
 * 
 * Button event that is posted to the bus and recieved by the 'ButtonEventListener'
 * 
 */

public class ButtonEvent extends CSEvent
{

    public ButtonEvent(String message) {
        super(message);
    }
}
