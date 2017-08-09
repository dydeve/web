package dydeve.rest.client;

/**
 * Created by yuduy on 2017/8/9.
 */
public interface HttpMethod {

    //GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE

    Get GET = new Get();
    Post POST = new Post();
    Head HEAD = new Head();
    Options OPTIONS = new Options();
    Put PUT = new Put();
    Patch PATCH = new Patch();
    Delete DELETE = new Delete();
    Trace TRACE = new Trace();

    class Get implements HttpMethod{
        private Get(){}
    }

    class Post implements HttpMethod{
        private Post(){}
    }

    class Head implements HttpMethod{
        private Head(){}
    }

    class Options implements HttpMethod{
        private Options(){}
    }

    class Put implements HttpMethod{
        private Put(){}
    }

    class Patch implements HttpMethod{
        private Patch(){}
    }

    class Delete implements HttpMethod{
        private Delete(){}
    }

    class Trace implements HttpMethod{
        private Trace(){}
    }

}
