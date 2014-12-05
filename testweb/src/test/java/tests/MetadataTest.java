package tests;


public class MetadataTest
{
  // @Test
  // public void test() throws IOException
  // {
  // // Create resources (repo & transaction) to persist a schema.
  // Package repo = newRepo();
  // Transaction<Object> tx = repo.connect(this.getClass().getName());
  // // Get a schema to play with...
  // Package pkg = new ISourcePackage(IBaseMetamodel.INSTANCE, new File(
  // "../objectof.derived/src/main/resources/packages/test.xml"));
  // // Create a resource to populate the schema...
  // ISchemaBean sch = (ISchemaBean) tx.create("Schema");
  // sch.setName(pkg.getUniqueName());
  // sch.setRelease(Long.toHexString(System.currentTimeMillis()));
  // @SuppressWarnings("unchecked")
  // Map<String, SchemaMember> members = (Map<String, SchemaMember>) tx
  // .create("Schema.members");
  // sch.setMembers(members);
  // long idx = 0L;
  // for (Kind<?> kind : pkg.getMembers())
  // {
  // String memberName = kind.getName();
  // ISchemaMemberBean member = (ISchemaMemberBean) tx
  // .create("Schema.members.schemaMember");
  // members.put(memberName, member);
  // member.setComponentName(memberName);
  // member.setStereotype(kind.getStereotype().toString());
  // member.setIndex(++idx);
  // @SuppressWarnings("unchecked")
  // Map<String, String> props = (Map<String, String>) tx
  // .create("Schema.members.schemaMember.properties");
  // member.setProperties(props);
  // for (String pname : kind.getPropertyNames())
  // {
  // props.put(pname, kind.getProperty(pname).getSource());
  // }
  // }
  // tx.post();
  // tx.close();
  // tx = repo.connect("");
  // Object result = tx.retrieve("Schema", "1");
  // CreateRepo.print(tx, result);
  // }
  //
  // protected Package newRepo()
  // {
  // IPackage schema = new ISourcePackage(IBaseMetamodel.INSTANCE, new File(
  // "../objectof.derived/src/main/resources/packages/sch.xml"));
  // return newRepo(schema);
  // }
  //
  // protected Package newRepo(IPackage aSchema)
  // {
  // ISqlDb db = new ISqlDb("testDatabase");
  // return db.createPackage(
  // "test.objectof.org:1401/res/schema/" + System.currentTimeMillis(),
  // IRip.class.getName(), aSchema);
  // }
}
