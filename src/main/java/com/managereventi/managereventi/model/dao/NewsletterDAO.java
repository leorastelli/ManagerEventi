package com.managereventi.managereventi.model.dao;

import java.util.List;
import com.managereventi.managereventi.model.mo.Newsletter;

public interface NewsletterDAO {
    public Newsletter subscribeToNewsletter(String idUtente, String idEvento);
    public void unsubscribeFromNewsletter(String idUtente, String idEvento);
    public boolean isSubscribed(String idUtente, String idEvento);
    public List<String> getSubscribedEvents(String idUtente);
    public List<String> getSubscribers(String idEvento);
}

