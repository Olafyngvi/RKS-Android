using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Massage.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Massage.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ArtikalController : ControllerBase
    {
        private readonly MojContext _context;

        public ArtikalController(MojContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IEnumerable<Artikal> GetAll()
        {
            return _context.artikli.ToList();
        }
    }
}