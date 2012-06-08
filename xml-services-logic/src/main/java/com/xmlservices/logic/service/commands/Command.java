package com.xmlservices.logic.service.commands;

/**
 * Command that can be applied on XML files.
 *
 * @author Sergiu Indrie
 */
public interface Command {

    void execute() throws Exception;
}
