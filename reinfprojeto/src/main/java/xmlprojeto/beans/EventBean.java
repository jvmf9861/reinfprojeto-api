package xmlprojeto.beans;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class EventBean {
    private String selectedEvent;
    private List<String> events;

    public EventBean() {
        events = new ArrayList<>();
        events.add("DDF009");
        events.add("DDF021");
        events.add("DDF025");
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}

