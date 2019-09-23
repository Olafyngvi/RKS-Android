using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Massage.Models
{
    public class MojContext : DbContext
    {

        public MojContext(DbContextOptions<MojContext> options)
            : base(options) { }

        public DbSet<Korisnik> korisnici { get; set; }
        public DbSet<Termini> termini { get; set; }
        public DbSet<Rezervacije> rezervacije { get; set; }
        public DbSet<Artikal> artikli { get; set; }


    }
}
