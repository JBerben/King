package king.org.trans;

import org.objectweb.asm.tree.ClassNode;

import king.org.Updater;

import java.util.Collection;

public abstract class Transform {
    public Updater instance;

    public Transform(Updater i) {
        instance = i;
    }

    public abstract ClassNode identify(Collection<ClassNode> classNodes);

    public abstract ClassNode manipulate(ClassNode classNode);
}
