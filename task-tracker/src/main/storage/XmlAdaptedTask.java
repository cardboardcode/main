package main.storage;

import main.commons.exceptions.IllegalValueException;
import main.model.task.*;
//import main.model.tag.Tag;
//import main.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private String message_;
    @XmlElement(required = true)
    private int taskID;
    //@XmlElement(required = true)
    //private String email;
    //@XmlElement(required = true)
    //private String address;

    //@XmlElement
    //private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        message_ = source.getMessage().toString();
        taskID = source.getTaskID();
        //phone = source.getPhone().value;
        //email = source.getEmail().value;
        //address = source.getAddress().value;
        //tagged = new ArrayList<>();
        //for (Tag tag : source.getTags()) {
          //  tagged.add(new XmlAdaptedTag(tag));
        //}
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Task toModelType() throws IllegalValueException {
        //final List<Tag> personTags = new ArrayList<>();
        //for (XmlAdaptedTag tag : tagged) {
          //  personTags.add(tag.toModelType());
        //}
//        final Name name = new Name(this.name);
//        final Phone phone = new Phone(this.phone);
//        final Email email = new Email(this.email);
//        final Address address = new Address(this.address);
//        final UniqueTagList tags = new UniqueTagList(personTags);
          final Message message = new Message(message_);
          
          return new Task(taskID, message);
    }
}
